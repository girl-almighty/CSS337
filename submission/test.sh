#! /bin/bash

echo "b
10
" | python3 otp_client.py

echo; echo "---Printing contents of Client output file---"
cat output.txt

javac OTP_Server.java; java OTP_Server < output.txt
