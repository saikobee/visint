#!/usr/bin/ruby

include Math

#X = 51
#Z = 81

#X = 75
#Z = 75

X = 150
Z = 150

def f x, z
    # This centers the function
    x -= X/2.0
    z -= Z/2.0

    x /= 10.0
    z /= 10.0

    #cos(x**2 + z**2) * x**2 * z**2
    #cos(x**2 + z**2) - x**2 - z**2
    sin(x * z) * (x**2 + z**2)
end

out =
    (0 ... X).map{|x|
        (0 ... Z).map{|z|
            f(x, z).to_f
        }
    }

puts out.map{|row| row.join(" ")}.join("\n")
