<p align="center"><img src="docs/logo.svg" width="96" alt="" /></p>

# netsieve

[![CI](https://github.com/Gulliby-development/netsieve/actions/workflows/ci.yml/badge.svg)](https://github.com/Gulliby-development/netsieve/actions/workflows/ci.yml) [![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

netsieve is a small command-line program written in Scala 3 (standard library only). It reads a list of IPv4 networks on standard input, one per line, and can validate, sort, merge, count, cut up and search that list.

It is for people who keep firewall rule sets, routing tables and allow lists and who work in a terminal, in a script or in CI. It needs no network and no clock.

To run it you need a JDK 17 or newer; it runs on Windows, macOS and Linux. Compile it, take the class path from sbt and start the main class:

```sh
sbt --server -batch compile
CP=$(sbt --server -batch -error showClasspath)
java -cp "$CP" netsieve.Main COMMAND
```

## Usage

```text
usage: netsieve check
       netsieve sort [--style STYLE]
       netsieve merge [--style STYLE]
       netsieve count [--mode MODE]
       netsieve gaps --within NET [--style STYLE]
       netsieve contains --ip IP
       netsieve split --prefix N [--style STYLE]
```

The commands, in this order:

1. `check` validates the list.
2. `sort` orders the networks.
3. `merge` writes the fewest networks that cover the same addresses.
4. `count` counts the covered addresses.
5. `gaps` lists the addresses of a given network that the list does not cover.
6. `contains` names the networks that hold an address.
7. `split` cuts every network into smaller ones.

Option rules:

- `--style` is one of `cidr`, `range`, `mask`, `hex`. The default is `cidr`.
- `--mode` is one of `addresses`, `hosts`. The default is `addresses`.
- `--prefix` is a whole number from 0 to 32 with one or two digits and no leading zero (`0` is fine, `05` is not).
- `--within` is one network written like a line of the list.
- `--ip` is one address (four numbers, no `/`).
- Every option takes the next word as its value. There is no `--option=value` form.
- Each command accepts only its own options (see the synopsis); `check` accepts none. `gaps` needs `--within`, `contains` needs `--ip` and `split` needs `--prefix`.
- `help`, `-h` and `--help` as the first word print the usage and exit 0. `--version` prints `netsieve 0.1.0` and exits 0. With no word the usage goes to standard error and the exit status is 2.

The command line is checked first (nothing is read when it is wrong), then the list, then the command runs.

## Networks

The input is cut into lines at each line feed; a last line without a line feed counts as a line, and one carriage return at the end of a line is dropped. An input of more than 40 lines is an error (exactly 40 is fine), and skipped lines count.

- A line that holds only spaces and tabs, or whose first word starts with `#`, is skipped.
- Every other line is exactly one network word. Spaces and tabs at the ends are ignored; more than one word is an error.
- A list without any network is an error.

A network is an address, or an address, a `/` and a prefix length. An address is four numbers separated by `.`. Each number is `0` or one to three digits with no leading zero and no sign, and at most 255 (`01` and `256` are bad). A prefix length is `0` or one or two digits with no leading zero, at most 32 (`33`, `033` and `-1` are bad). An address without `/` is the network of one address, prefix length 32.

A network with host bits set is rejected: `10.0.0.1/24` is an error, `10.0.0.0/24` is fine, and a prefix length of 32 never has host bits.

## Styles

Every network is written in one style. A.B.C.D is the first address in dotted form.

- `cidr`: `A.B.C.D/LENGTH`, for example `10.0.0.0/8`; one address is `1.2.3.4/32`.
- `range`: the first and the last address, for example `10.0.0.0-10.255.255.255`; one address is `1.2.3.4-1.2.3.4`.
- `mask`: the first address, a space and the netmask, for example `10.0.0.0 255.0.0.0`.
- `hex`: the first address as eight upper case hexadecimal digits, a `/` and the prefix length, for example `0A000000/8`.

## Output

A command that works writes its result to standard output and nothing to standard error. Counts use the singular for 1 and the plural otherwise: `1 network`, `3 networks`.

- `check` prints `ok, N networks` (skipped lines do not count), for example `ok, 1 network`.
- `sort` prints every network, ordered by first address and then by prefix length (shorter first), in the style, one per line. Equal networks stay.
- `merge` prints the fewest networks that cover exactly the same addresses, ordered by first address. Overlapping or touching networks are joined first. Two neighbours that do not make one network stay two: `10.0.0.128/25` and `10.0.1.0/25` stay as they are.
- `count` prints the number of covered addresses, each counted once. With `--mode hosts` each merged network counts its addresses minus 2 when its prefix length is 30 or shorter, and all its addresses for 31 and 32: `10.0.0.0/24` has 254 hosts, `10.0.0.0/31` has 2 and `10.0.0.5` has 1.
- `gaps` prints the addresses of the `--within` network that no listed network covers, as the fewest networks in the style. Networks outside it are ignored and a network that sticks out covers only the part inside. When nothing is left it prints `none`.
- `contains` prints one line `IP in NET` for each network of the list that holds the address, in input order, with NET in the `cidr` style. When no network holds the address it prints `IP is not covered` on standard output and exits 1.
- `split` prints every network cut into networks of the given prefix length, in input order and ascending within a network. A network that already has that length stays. A network longer than the prefix is an error, and so is an output of more than 256 lines (exactly 256 is fine).

## Examples

In these examples `netsieve` stands for a shell function that runs `java -cp "$CP" netsieve.Main "$@"`, where `CP` holds the output of `sbt --server -batch -error showClasspath`. They use a POSIX shell in the repository directory; `2>&1` shows the error line.

```text
$ printf '10.0.0.0/25\n10.0.0.128/25\n10.0.1.0/24\n' | netsieve merge
10.0.0.0/23
$ printf '10.0.0.0/24\n192.168.1.0/30\n' | netsieve sort --style range; printf '172.16.0.0/12\n' | netsieve sort --style mask
10.0.0.0-10.0.0.255
192.168.1.0-192.168.1.3
172.16.0.0 255.240.0.0
$ printf '10.0.0.0/24\n10.0.0.64/26\n' | netsieve gaps --within 10.0.0.0/23; printf '10.0.0.0/30\n' | netsieve split --prefix 31 --style hex
10.0.1.0/24
0A000000/31
0A000002/31
$ printf '10.0.0.0/8\n10.0.0.1/24\n' | netsieve check 2>&1; printf '10.0.0.0/8\n' | netsieve contains 2>&1
netsieve: line 2: host bits set 10.0.0.1/24
netsieve: missing --ip
```

## Errors

Exit statuses:

- 0: success.
- 1: an error in the list or in a command; also `contains` when no network holds the address (its normal output is still written).
- 2: an error in the command line.

An error writes nothing to standard output and one line `netsieve: MESSAGE` to standard error. The messages:

- Command line (exit 2): `unknown command WORD`, `unexpected argument WORD`, `unknown option WORD`, `option OPTION is not valid for COMMAND`, `given twice: OPTION`, `missing value for OPTION`, `bad value for OPTION`, `missing --within`, `missing --ip`, `missing --prefix`.
- List (exit 1): `input is too long (max 40 lines)`, `line N: expected 1 network`, `line N: bad network WORD`, `line N: host bits set WORD`, `no networks`.
- Commands (exit 1): `line L: cannot split WORD into /N`, `too many subnets (max 256)`.

N and L are line numbers counted from 1, skipped lines included. WORD is the word exactly as written. The first problem found is the error.

## Development

```sh
sbt --server -batch scalafmtCheckAll
sbt --server -batch testFull
```

The tests are literal rows that run command lines through `Cli.run`.

---

Built with [BAKED](https://github.com/Gulliby-development/baked), an open-source coding agent (bench round R106).
