Integer extend [
    bernoulli [
        | b len |
        len := self + 1.
        b := Array new: len.
        b at: 1 put: 1.
        2 to: len do: [:m | 
            b at: m put: 0.
            1 to: (m - 1) do: [:k | 
                b at: m put: (b at: m) - ((m binom: (k - 1)) * (b at: k))].
            b at: m put: (b at: m) / m].
        ^(b at: len)
    ]
    
    binom: k [
        | r |
        r := 1.
        1 to: k do: [:i | r := r * (self - i + 1) / i].
        ^r
    ]
]

(stdin nextLine asInteger bernoulli) printNl.
