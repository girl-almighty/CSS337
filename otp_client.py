#otp_client.py
#author: Jon Dycaico

import hashlib as hl
import sys

hashes = []
keys = []

seed = '808670FF00FF08812'

def generate_otp():
    if len(hashes) == 0:
        new_hash = hl.sha256(seed.encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    else:
        new_hash = hl.sha256(hashes[-1].encode()).hexdigest()
        new_key = str(int(new_hash, 16))[:6]
    hashes.append(new_hash)
    keys.append(new_key)

def usermode():
    print('\033[95m' + 'USER MODE' + '\033[0m')
    print('\033[95m' + '(y to proceed, n to quit)' + '\033[0m' + '\n')
    response = input('generate key? ')
    while response.lower() != 'n':
        generate_otp()
        print('    OTP ' + str(len(hashes)) + ': ' + '\033[1m' + str(keys[-1]) + '\033[0m')
        response = input('generate key? ')

def bulkmode():
    print('\033[95m' + 'BULK MODE' + '\033[0m')
    count = input('\033[95m' + 'Enter the number of OTPs you want to generate:' + '\033[0m' + '  ')
    for i in range(int(count)):
        generate_otp()
    print('\nKeys:')
    print(str(keys))


print('\033[93m' + '--- OTP client ---' + '\033[0m')
modeset = input('\033[93m' + 'Input \'a\' for user mode or \'b\' for bulk mode  ' + '\033[0m')
if modeset.lower() == 'a':
    usermode()
elif modeset.lower() == 'b':
    bulkmode()
else:
    print('invalid mode selected')
    sys.exit(-1)

print('\nHashes:')
for element in hashes:
    print('    ' + element)
