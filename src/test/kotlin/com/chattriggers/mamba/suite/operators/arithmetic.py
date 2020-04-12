a = 5
b = 17

assert a + b == 22, "a + b"
assert b + a == 22, "b + a"
assert a - b == -12, "a - b"
assert b - a == 12, "b - a"
assert a * b == 85, "a * b"
assert b * a == 85, "b * a"
assert a != b, "a != b"
assert b != a, "b != a"
assert 1 < a < 10 < b < 17, "chained <"
assert 0.2941176 < a / b, "a / b (1)"
assert a / b < 0.2941177, "a / b (2)"
assert b / a == 3.4, "b / a"
assert 2 ** 3 == 8, "2 ** 3"
