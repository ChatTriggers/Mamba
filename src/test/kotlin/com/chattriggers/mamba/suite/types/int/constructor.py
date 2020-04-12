assert int(5) == 5
assert int(-5) == -5
assert int(5.4) == 5
assert int(-5.4) == -5
assert int(5.9) == 5
assert int(-5.9) == -5
assert int('5') == 5
assert int('-5') == -5

# assertFails int("5.9")
# assertFails int("-5.9")

# assert int('142', 9) == 119
# assert int('0', 16) == 0
# assert int('+0', 16) == 0
# assertFails int('++0', 16)
# assert int('154', base=6) == 70
# assert int('-154', base=6) == -70
# assertFails int('154', base=5)
# assert int('0x65', 16) == 101
# assertFails int('0x65')
# assert int('0b011101010', 2) == 234
# assertFails int('0b011101010')
# assert int('0o124', 8) == 84
