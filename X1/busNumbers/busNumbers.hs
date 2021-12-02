import           Data.List

main = do
    _      <- getLine
    busNrs <- getLine
    putStrLn
        . unwords
        . concat
        $ (map lineCheck . (groupWhen oneWithin) . (sortBy numberComp) . words)
              busNrs

numberComp :: String -> String -> Ordering
numberComp x y = (read x :: Int) `compare` (read y :: Int)

lineCheck :: [String] -> [String]
lineCheck x = if length x > 2 then [(head x) ++ "-" ++ (last x)] else x

oneWithin :: String -> String -> Bool
oneWithin x y = abs ((read x) - (read y)) <= 1

groupWhen :: Eq a => (a -> a -> Bool) -> [a] -> [[a]]
groupWhen _ []  = []
groupWhen _ [a] = [[a]]
groupWhen f (a : l) | a == (head c) = (c : r)
                    | f a (head c)  = (a : c) : r
                    | otherwise     = [a] : c : r
    where (c : r) = groupWhen f l
