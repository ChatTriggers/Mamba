assert (5).__abs__() == 5
assert (-5).__abs__() == 5
assert (4).__add__(8) == 12
assert (-1).__add__(1) == 0
assert (12).__and__(6) == 4
assert not (0).__bool__()
assert (1).__bool__()
assert (-4).__bool__()
assert (-4).__ceil__() == -4
assert (4).__ceil__() == 4
assert (6).__eq__(6)
assert not (6).__eq__(-6)
# assert type((6).__float__()) is float
assert (4).__floor__() == 4
assert (-4).__floor__() == -4
assert (5).__floordiv__(2) == 2
# assert (5).__floordiv__(2.1) is NotImplemented

