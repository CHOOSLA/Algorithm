
a = [1,2]
b = (1,2)

x,y = a
print(x)

print(list(map(lambda x,y: x+y,a,b)))

if a==b:
    print('yes')