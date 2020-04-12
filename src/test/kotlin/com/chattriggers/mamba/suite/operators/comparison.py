a = 5
b = 17

assert a < b, "a < b"
assert a <= b, "a <= b"
assert a <= 5, "a <= 5"
assert b > a, "b > a"
assert b >= a, "b >= a"
assert b >= 17, "b >= 17"
assert 1 < a < 10 < b < 17, "chained <"
assert 0.2941176 < a / b, "a / b (1)"