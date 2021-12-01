-- Av: David Ljunggren och Timmy Lindholm
module Main where
import Control.Concurrent -- for threadDelay
import Control.Monad.State -- for liftIO
import UI.NCurses
--En blockade datatyp har en position, en riktning och en historik
data Blockade = Blockade {position :: (Integer, Integer), direction :: Int, positionHistory :: [(Integer, Integer)]} deriving (Eq, Show, Read)

main :: IO () -- mainfunktion. the only IO action which can really be said to run in a compiled Haskell program is main
main = runCurses ( do
    -- Stage color
    setEcho False -- Så vi inte skriver ut inputs i terminalen (kan interferera med ncurses-grafik)
    stageColor <- newColorID ColorGreen ColorBlack 1 -- Skicka färginfo (monadisk bindning) till variabeln stageColor. (den kommer få samma typ som monaden i HL, Curses ColorID)
    gameWindow <- defaultWindow -- Fönstret som ncurses ska skapa (<- binds the result of a monadic operation in the current monad to a name.)
    let blockades = [Blockade (7, 7) 1 [], Blockade (5, 195) 3 []] -- pos i (y,x)
    updateWindow gameWindow ( do -- updateWindow tar Window -> Update a -> Curses a
      drawMap (Glyph '#' [AttributeColor stageColor])) -- drawMap tar in en glyph
        -- Draws the border around the window (stage)
    --render -- Rita eventuella uppdateringar
    gameLoop gameWindow blockades -- kör spelloopen på terminalfönstret och blockades-listan
    updateWindow gameWindow clear -- cleara fönstret efter ett game, annars blir det kladdigt
    liftIO $ threadDelay 1000000) -- liten delay innan avslut. LiftIO lifts a computation from the IO monad.

gameLoop :: Window -> [Blockade] -> Curses () -- Curses () en wrapper-datatyp kring IO. Ser till att ncurses körs när programmet körs.
gameLoop gameWindow x = loop x where
    loop x = do -- loop tar in en lista x av blockader. Index 0 blir spelare 1 och index 1 spelare 2.
        colorBody1 <- newColorID ColorGreen ColorGreen 2 -- Färg för spelare 1. newColorID foreGround backGround ID. (färgar en textruta i terminalen. Detta utgör ett blockad-element)
        colorBody2 <- newColorID ColorGreen ColorGreen 3 -- Färg för spelare 2.
        --  Nya blockadpositionerna kommer från generateBlockade
        -- blockades returnerar en "update a" i form av do, men slutreturnen kommer vara en lista.
        blockades <- updateWindow gameWindow $ do
          mapSize <- windowSize -- Fönsterstorleken i (ykoord, xkoord)
          moveCursor 1 62
          drawString "Welcome to Blockade. Press X to exit at any time."
          -- Om blockaderna inte krockat, rita ut nya
          if checkCollision mapSize x then do -- checkCollision är en Boolean. om true genererar vi blockades och spelar på
             a <- generateBlockade colorBody1 (dataAtIndex 0 x) -- a blir anropet generateBlockade på colorBody1 och blockaden på index 0 i x.
             b <- generateBlockade colorBody2 (dataAtIndex 1 x) -- b blir anropet generateBlockade på colorBody2 och blockaden på index 1 i x.
             return [a,b] -- returnera [Blockade]-listan [a,b]
          else do -- om checkCollision är false har vi krockat och returnerar den tomma listan.  
            moveCursor 22 70 
            drawString "Game over! Press X to exit."
            return []

        render -- Rita eventuella uppdateringar
        -- Spelhastigheten kan justeras med threadDelay.
        liftIO $ threadDelay 100000 -- LiftIO lifts a computation from the IO monad.
        inputEvent <- getEvent gameWindow (Just 5) -- getEvent tar in ett fönster och en timeout i millisek. Högre timeout - långsammare spel. If the timeout is Nothing, getEvent blocks until an event is received.
        -- Om blockades är tom har blockader krockat. Loopa inte. 
        if blockades == [] then return ()
        -- Annars loopa och kolla keyEvents
        else case inputEvent of -- inputEvent returnerar datatypen Curses (Maybe Event) (VL)
            -- Keyevents. Om inputEvent returnerar datatypen Curses (Maybe Event) i  VL, gör HL.
            Nothing -> loop blockades -- If the timeout is Nothing, getEvent blocks until an event is received.
            Just (EventCharacter 'x') -> return () -- x to quit game.
            Just (EventCharacter 'X') -> return () -- X to quit game.
             -- Styrning för spelare höger. Blockad-listan motsvarar listan [a,b] från blockades där a är player 1 och b player 2. direction = 0,1,2,3, blir index i head i generateBlockade
            Just (EventSpecialKey KeyDownArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 0}] -- Vid input DOWN, sätt den player1 blockadens riktning till 0.
            Just (EventSpecialKey KeyRightArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 1}] -- Vid input RIGHT, sätt den player1 blockadens riktning till 1.
            Just (EventSpecialKey KeyUpArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 2}] -- Vid input UP, sätt den player1 blockadens riktning till 2.
            Just (EventSpecialKey KeyLeftArrow) -> loop [(dataAtIndex 0 blockades), (dataAtIndex 1 blockades) {direction = 3}] -- Vid input LEFT, sätt den player1 blockadens riktning till 3.
            -- Styrning för spelare vänster. blockad-listan motsvarar listan [a,b] från blockades där a är player 1 och b player 2. 
            Just (EventCharacter 'w') -> loop [(dataAtIndex 0 blockades) {direction = 2}, (dataAtIndex 1 blockades)] -- Vid input w, sätt den player1 blockadens riktning till 2. (Blir index i tuple-listan direction i generateBlockade)
            Just (EventCharacter 'a') -> loop [(dataAtIndex 0 blockades) {direction = 3}, (dataAtIndex 1 blockades)] -- Vid input a, sätt den player1 blockadens riktning till 3
            Just (EventCharacter 's') -> loop [(dataAtIndex 0 blockades) {direction = 0}, (dataAtIndex 1 blockades)] -- Vid input s, sätt den player1 blockadens riktning till 0
            Just (EventCharacter 'd') -> loop [(dataAtIndex 0 blockades) {direction = 1}, (dataAtIndex 1 blockades)] -- Vid input d, sätt den player1 blockadens riktning till 1

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
     --Ritar ut en blockade och en pil
      moveCursor (fst p) (snd p) -- flytta cursorn till aktuell x och y pos. (fst ger y och snd ger x  i position(y,x))
      drawGlyph (Glyph '█' [AttributeColor colorBody]) -- rita ut en glyph med den givna färgen █ (full Unicode block) innan huvudet. (Spelar egentligen ingen roll för kroppen. ColorBody kommer färga hela rutan ändå.)
      moveCursor (fst deltaPosition) (snd deltaPosition) -- flytta cursorn till ny position (någpnstans +-1) och rita ut huvudet.
      drawString head -- och rita ut huvudet där (en pil)
      return $ Blockade deltaPosition d (p:pH) -- Returnera en blockade med positionen deltaPositiion, directionen d och (positionhistory med nytt huvud p)
  where -- Huvuden betraktas som strängarna nedan
        headUp = "^"
        headDown = "v"
        headLeft = "<"
        headRight = ">"
        head = dataAtIndex d [headDown, headRight, headUp, headLeft] -- head är det element som har index d.
        deltaPosition = getNewPosition (dataAtIndex d [(1,0), (0,1), (-1,0), (0,-1)]) p -- new position is old position +-1 in desired direction.
--Att ner (headDown) blir (1,0) tex är för att ncurses använder (y,x) så (1,0) betyder ett steg ned.

--Tar in en riktning och en position och räknar ut en ny position. Den nya pos blir den gamla +-1 i önskad riktning.
getNewPosition :: (Integer, Integer) -> (Integer, Integer) -> (Integer, Integer)
getNewPosition (d1, d2) (p1, p2) = (d1 + p1, d2 + p2)

--Standard kollisionskoll, false om krockat, annars True och vi får gå vidare. fst pos ger x-koord, snd pos ger y-koord
checkCollision :: (Integer, Integer) -> [Blockade] -> Bool -- Tar in 
checkCollision mapSize [Blockade {position = pos1, direction = _, positionHistory = posHis1}, Blockade {position = pos2, direction = _, positionHistory = posHis2}] =
  let posHis = (++) posHis1 posHis2 in -- posHis blir alla element som finns i antingen posHis1 eller posHis2, dvs alla tidigare positioner för båda spelarna.
   if fst pos1 == 0 || snd pos1 == 0 || fst pos1 == fst mapSize - 1 || snd pos1 == snd mapSize - 1 || elem pos1 posHis then False -- om p1 är precis innan tak / vänstervägg || golv / högervägg || där någon redan varit returnera false 
  else if fst pos2 == 0 || snd pos2 == 0|| fst pos2 == fst mapSize - 1|| snd pos2 == snd mapSize - 1 || elem pos2 posHis then False --  om p2 är precis innan tak / vänstervägg || golv / högervägg  || där någon redan varit returnera false
  else True -- Annars fritt fram att röra oss

--Rekursiv funktion som hämtar det n:te elementet ur en lista. Snabbare än "!!". 
dataAtIndex :: Int -> [a] -> a -- ta in en int och en lista av någonting.
dataAtIndex _ [] = error "Empty List!" -- om listan är tom printa detta
dataAtIndex y (x:xs) | y <= 0 = x -- om y är mindre eller lika med noll, returnera huvudet basfall.
                 | otherwise = dataAtIndex (y-1) xs  -- annars, gör samma sak som ovan med svansen och minska indextalet med ett.