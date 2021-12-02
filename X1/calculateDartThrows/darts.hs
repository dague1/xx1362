import           Data.List                     as List

scores = [1 .. 20]
d = [1 .. 3]

main = do
    input <- getLine
    let answer | not $ null one   = printOne $ head one
               | not $ null two   = printTwo $ head two
               | not $ null three = printThree $ head three
               | otherwise      = "impossible"
          where
            tar   = (read input)
            one   = oneThrow tar
            two   = twoThrows tar
            three = threeThrows tar

    putStrLn answer

threeThrows :: Int -> [((Int, Int), (Int, Int), (Int, Int))]
threeThrows targetScore =
    [ ((x, d1), (y, d2), (z, d3))
    | d1 <- d
    , d2 <- d
    , d3 <- d
    , x  <- scores
    , y  <- scores
    , z  <- scores
    , x * d1 + y * d2 + z * d3 == targetScore
    ]

twoThrows :: Int -> [((Int, Int), (Int, Int))]
twoThrows targetScore =
    [ ((x, d1), (y, d2))
    | x  <- scores
    , y  <- scores
    , d1 <- d
    , d2 <- d
    , x * d1 + y * d2 == targetScore
    ]

oneThrow :: Int -> [(Int, Int)]
oneThrow targetScore =
    [ (x, d1) | x <- scores, d1 <- d, x * d1 == targetScore ]

printThree (x, y, z) =
    (multiToString $ snd x)
        ++ (show $ fst x)
        ++ "\n"
        ++ (multiToString $ snd y)
        ++ (show $ fst y)
        ++ "\n"
        ++ (multiToString $ snd z)
        ++ (show $ fst z)

printOne (x) = (multiToString $ snd x) ++ (show $ fst x)

printTwo (x, y) =
    (multiToString $ snd x)
        ++ (show $ fst x)
        ++ "\n"
        ++ (multiToString $ snd y)
        ++ (show $ fst y)

multiToString :: Int -> String
multiToString m | m == 1 = "single "
                | m == 2 = "double "
                | m == 3 = "triple "
