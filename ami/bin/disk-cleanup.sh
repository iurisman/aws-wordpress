#!/bin/bash

# From
# https://itsfoss.com/free-up-space-ubuntu-linux/

# APT
sudo apt-get autoremove
sudo apt-get clean


# Clear systemd journal logs
sudo journalctl --vacuum-time=3d


# Removes old revisions of snaps
# CLOSE ALL SNAPS BEFORE RUNNING THIS
set -eu
snap list --all | awk '/disabled/{print $1, $3}' |
    while read snapname revision; do
        snap remove "$snapname" --revision="$revision"
    done


#
rm -rf ~/.cache/thumbnails/*


# From
# https://itsfoss.com/fix-empty-trash-ubuntu

rm -rf ~/.local/share/Trash/*


# https://docs.conda.io/projects/conda/en/latest/commands/clean.html
conda clean -ay
