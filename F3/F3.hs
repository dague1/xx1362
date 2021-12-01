-- Av: David Ljunggren och Timmy Lindholm
module Main where
import Control.Concurrent 
import Control.Monad.State 
import UI.NCurses

data Blockade = Blockade {position :: (Integer, Integer), direction :: Int, positionHistory :: [(Integer, Integer)]} deriving (Eq, Show, Read)

main :: IO () -- mainfunktion. the only IO action which can really be said to run in a compiled Haskell program is main
main = runCurses ( do
    -- Stage color
    setEcho False 
    stageColor <- newColorID ColorGreen ColorBlack 1 
    gameWindow <- defaultWindow
    let blockades = [Blockade (7, 7) 1 [], Blockade (5, 195) 3 []] 
    updateWindow gameWindow ( do -- updateWindow tar Window -> Update a -> Curses a
      drawMap (Glyph '#' [AttributeColor stageColor])) 
        -- Draws the border around the window (stage)
    --render -- Rita eventuella uppdateringar
    gameLoop gameWindow blockades
    updateWindow gameWindow clear 
    liftIO $ threadDelay 1000000) 

gameLoop :: Window -> [Blockade] -> Curses () 
gameLoop gameWindow x = loop x where
    loop x = do -- loop tar in en lista x av blockader. Index 0 blir spelare 1 och index 1 spelare 2.
        colorBody1 <- newColorID ColorGreen ColorGreen 2 
        colorBody2 <- newColorID ColorGreen ColorGreen 3 
        --  Nya blockadpositionerna kommer från generateBlockade
        -- blockades returnerar en "update a" i form av do, men slutreturnen kommer vara en lista.
        blockades <- updateWindow gameWindow $ do
          mapSize <- windowSize -- Fönsterstorleken i (ykoord, xkoord)
          moveCursor 1 62
          drawString "Welcome to Blockade. Press X to exit at any time."
          -- Om blockaderna inte krockat, rita ut nya
          if checkCollision mapSize x then do -- checkCollision är en Boolean. om true genererar vi blockades och spelar på
             a <- generateBlockade colorBody1 (dataAtIndex 0 x) 
             b <- generateBlockade colorBody2 (dataAtIndex 1 x) 
             return [a,b] 
          else do 
            moveCursor 22 70 
            drawString "Game over! Press X to exit."
            return []

        render 
        
        liftIO $ threadDelay 100000 
        inputEvent <- getEvent gameWindow (Just 5) 
        
        if blockades == [] then return ()
        
        else case inputEvent of 
           
            Nothing -> loop blockades 
            Just (EventCharacter 'x') -> return () 
            Just (EventCharacter 'X') -> return () 
            
            Just (EventSpecialKey KeyDownArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 0}] 
            Just (EventSpecialKey KeyRightArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 1}]
            Just (EventSpecialKey KeyUpArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 2}]
            Just (EventSpecialKey KeyLeftArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 3}] 
           
            Just (EventCharacter 'w') -> loop [(dataAtIndex 0 blockades) {direction = 2}, (dataAtIndex 1 blockades)] 
            Just (EventCharacter 'a') -> loop [(dataAtIndex 0 blockades) {direction = 3}, (dataAtIndex 1 blockades)] 
            Just (EventCharacter 's') -> loop [(dataAtIndex 0 blockades) {direction = 0}, (dataAtIndex 1 blockades)]
            Just (EventCharacter 'd') -> loop [(dataAtIndex 0 blockades) {direction = 1}, (dataAtIndex 1 blockades)] 

--Function that draws the map using glyph g and nCurses function drawBorder. Uses the monad and datatype Maybe. Update () is an nCurses monad and datatype.  
drawMap :: Glyph -> Update ()
drawMap g = 
        drawBorder (Just g) -- nCurses-funktion. Tar in en glyph g och ritar den runt fönstret. left edge
                   (Just g) -- right edge..
                   (Just g) -- top edge... etc
                   (Just g)
                   (Just g)
                   (Just g)
                   (Just g)
                   (Just g)

generateBlockade :: ColorID -> Blockade -> Update Blockade -- ColorID: A wrapper around Integer to ensure clients don’t use an uninitialized color in an attribute.
generateBlockade colorBody Blockade {position = p, direction = d, positionHistory = pH} =
  do
      moveCursor (fst p) (snd p) 
      drawGlyph (Glyph '█' [AttributeColor colorBody]) 
      moveCursor (fst deltaPosition) (snd deltaPosition) 
      drawString head -- och rita ut huvudet där (en pil)
      return $ Blockade deltaPosition d (p:pH) 
  where -- Huvuden betraktas som strängarna nedan
        headUp = "^"
        headDown = "v"
        headLeft = "<"
        headRight = ">"
        head = dataAtIndex d [headDown, headRight, headUp, headLeft]
        deltaPosition = getNewPosition (dataAtIndex d [(1,0), (0,1), (-1,0), (0,-1)]) p 



getNewPosition :: (Integer, Integer) -> (Integer, Integer) -> (Integer, Integer)
getNewPosition (d1, d2) (p1, p2) = (d1 + p1, d2 + p2)


checkCollision :: (Integer, Integer) -> [Blockade] -> Bool
checkCollision mapSize [Blockade {position = pos1, direction = _, positionHistory = posHis1}, Blockade {position = pos2, direction = _, positionHistory = posHis2}] =
  let posHis = (++) posHis1 posHis2 in 
   if fst pos1 == 0 || snd pos1 == 0 || fst pos1 == fst mapSize - 1 || snd pos1 == snd mapSize - 1 || elem pos1 posHis then False 
  else if fst pos2 == 0 || snd pos2 == 0|| fst pos2 == fst mapSize - 1|| snd pos2 == snd mapSize - 1 || elem pos2 posHis then False 
  else True 


dataAtIndex :: Int -> [a] -> a 
dataAtIndex _ [] = error "Empty List!" 
dataAtIndex y (x:xs) | y <= 0 = x
                 | otherwise = dataAtIndex (y-1) xs 
