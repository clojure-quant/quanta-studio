


 guix shell --development --check openjdk@17 clojure-tools



# THIS REPRODUCES THE BUG:

guix shell --pure --development --check clojure-tools openjdk coreutils


# THIS FIXES THE BUG


guix time-machine --commit=98e4bfe96f2c99daa0b66b65c1d379bb385301a8 -- guix shell --pure --development --check clojure-tools openjdk coreutils


# NIX

shell.nix
{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {
    # nativeBuildInputs is usually what you want -- tools you need to run
    nativeBuildInputs = with pkgs.buildPackages; [ ruby_3_2 ];
}
nix-shell shell.nix




nix-shell shell.nix