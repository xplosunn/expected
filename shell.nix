{ pkgs ? import (fetchTarball
  "https://github.com/NixOS/nixpkgs/archive/68165781ccbe4d2ff1d12b6e96ebe4a9f4a93d51.tar.gz")
  { } }:
let
  jre = pkgs.jre;
  sbt = pkgs.sbt.override { jre = jre; };
in
pkgs.mkShell {
  buildInputs = [
    jre
    sbt
  ];
}
