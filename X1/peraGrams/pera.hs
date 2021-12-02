import Data.List
main = do
  input <- getLine
  putStrLn . show $ max 0 (length (filter odd $ map length $ (group . sort) input) - 1)
