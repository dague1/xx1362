[kattio].
    
main :- read_int(Tar),
        (oneThrow(Tar)
         ;twoThrow(Tar)
         ;threeThrow(Tar)
         ;write('impossible')
        ), nl.
    
oneThrow(Tar) :- 
  bagof((X,D),
        (numlist(1,3,I), member(D,I),
         numlist(1,20,K), member(X,K),
         X*D=:=Tar),
        Darts),
  [H|T] = Darts,
  (X,D) = H,
  writeMulti(D), write(X).

twoThrow(Tar) :- 
  bagof((X,D1,Y,D2),
        (numlist(1,3,I), member(D1,I), member(D2,I),
         numlist(1,20,K), member(X,K), member(Y,K),
         X*D1+Y*D2=:=Tar),
        Darts),
  [H|T] = Darts,
  (X,D1,Y,D2) = H,
  writeMulti(D1), write(X), nl,
  writeMulti(D2), write(Y).

threeThrow(Tar) :- 
  bagof((X,D1,Y,D2,Z,D3),
        (numlist(1,3,I), member(D1,I),member(D2,I),member(D3,I),
         numlist(1,20,K), member(X,K),member(Y,K),member(Z,K),
         X*D1+Y*D2+Z*D3=:=Tar),
        Darts),
  [H|T] = Darts,
  (X,D1,Y,D2,Z,D3) = H,
  writeMulti(D1), write(X), nl,
  writeMulti(D2), write(Y), nl,
  writeMulti(D3), write(Z).

writeMulti(1) :- write("single ").
writeMulti(2) :- write("double ").
writeMulti(3) :- write("triple ").
