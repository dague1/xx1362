      ******************************************************************
      * Author:
      * Date:
      * Purpose:
      * Tectonics: cobc
      ******************************************************************
       IDENTIFICATION DIVISION.
       PROGRAM-ID. bernoulli. *>Program name
       AUTHOR. David Ljunggren.
       DATA DIVISION.  *> Data Division contains sections beneath.
       FILE SECTION.  *>Data sent and received from storage.
       WORKING-STORAGE SECTION. *> Define variables here.
       01 i      PIC S9(9). *> 9 whole numbers, the sign is remembered
       01 k      PIC S9(9).
       01 m      PIC S9(9).
       01 n      PIC S9(9).
       01 lst.
           05    b  USAGE COMP-2 VALUES ZEROS OCCURS 99 TIMES. *> b blir en lista av doubles,(COMP-2). storlek 99.
       01 r      USAGE COMP-2. *> USAGE specifierar hur data ska lagras i minnet.
       01 tmp    USAGE COMP-2.
       PROCEDURE DIVISION.
       *> Vi förskjuter allt 1 steg höger (vilket korrigeras för i binom)
       ACCEPT n *> Accept input variable n
       ADD 1 TO n
       MOVE 1 TO b(1) *> MOVE used to assign values.
       MOVE 2 TO m
       PERFORM WITH TEST BEFORE UNTIL m > n*> typ som en forLoop. WITH TEST BEFORE kollar och terminerar loopen om villkoret är TRUE.
               MOVE 1 TO k  *> k assigned to 1.
               PERFORM UNTIL k = m
                       *> calculates m choose k-1 (förskjutningen) and puts it in r
                       PERFORM BINOM  *>Statements in PERFORM are run until END PERFORM.
                       *> b[m] -= r*b[k]
                       MULTIPLY r BY b(k) GIVING tmp *> r multiplied by b(k) and stored in tmp.
                       SUBTRACT tmp FROM b(m) *> tmp subtracted from b(m)
                       ADD 1 TO k
               END-PERFORM
               DIVIDE m INTO b(m)*> b(m) divided by m and stored in b(m).
               ADD 1 TO m
       END-PERFORM
       DISPLAY b(n) *> Vi returnerar index n i listan b.
       GOBACK.
       BINOM. *> Beräknar binomialtalen
       MOVE 1 TO r
       MOVE 1 TO i
       SUBTRACT 1 FROM k  *> 1 subtracted from k
       PERFORM UNTIL i > k
           SUBTRACT i FROM m GIVING tmp *> i will be subtracted from m and store result in tmp. i does not change.
           ADD 1 TO tmp
           DIVIDE i INTO tmp *> tmp divided by i and stored in tmp.
           MULTIPLY tmp BY r *> tmp ggr r lagras i r.
           ADD 1 TO i
       END-PERFORM *> loop avslutas
       ADD 1 TO k.
       END PROGRAM bernoulli.
