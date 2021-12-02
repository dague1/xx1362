[kattio].

main :-
    read_string(P),
    count_odd_occurrences(P, Odds),
    length(Odds, Len),
    A is max(0,Len-1),
    write(A), nl.
    
count_odd_occurrences(List, Occ):-
    findall([L], (bagof(true,member(X,List),Xs), length(Xs,L), L mod 2 =:= 1), Occ).
