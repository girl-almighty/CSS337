def collision_counter()
    unique_OTPs = []
    total_lines = 0

    file = open('output.txt', 'r')
    with open('output.txt') as file:
        for line in file:
            total_lines += 1
            if line not in unique_OTPs:
                unique_OTPs.append(line)

    collisions = total_lines - len(unique_OTPs)
    return collisions
