#!/usr/bin/bash

#This will integrate with TOC19 to allow the admin to copy the database to a USB flash drive. 
#should be run on Save database

# mount the drive
mount /dev/sda1 /mnt

# copy the databases
cp /home/TOC/adminPersonDatabase.txt /mnt/adminPersonDatabase.txt
cp home/TOC/adminProductDatabase.txt /mnt/adminPersonDatabase.txt

# Unmount the drive
umount /mnt
