#file = open('output2.txt')
#keys = file.readlines()
#file.close()
import numpy as np
keys = np.random.uniform(0,64,10000)

def multi_col_checker(two_keys):
    print(two_keys)
    count = -1
    for i in range(len(keys)):
        if keys[i] == two_keys[0] and keys[i+1] == two_keys[1]:
            count += 1
    return count

def multiple_collisions():
    multicol_count = 0
    exclusion_list = []
    for i in range(len(keys)-2):
        if keys[i:i+2] not in exclusion_list:
            multicol_count += multi_col_checker(keys[i:i+2])
            exclusion_list.append(keys[i:i+2])
    print('Collisions of sequential pairs: ' + str(multicol_count))

multiple_collisions()
