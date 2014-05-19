#!/usr/bin/bash

#This will integrate with TOC19 to allow the admin to copy the database to a USB flash drive. 
#should be run on Save database

# mount the drive
mount /dev/sd* /mnt

# copy the databases
cp /root/adminPersonDatabase.txt /mnt/adminPersonDatabase.txt
cp /root/adminProductDatabase.txt /mnt/adminPersonDatabase.txt

# Unmount the drive
umount /mnt
