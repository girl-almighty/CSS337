#! /bin/bash

echo b | python ./otp_client.py > client_keys.txt

javac OTP_Server.java; java OTP_Server < client_keys.txt
