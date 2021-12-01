-module(bernoulli).
-export([main/1]).

main(N) -> io:fwrite("~f~n", [bernoulli(N)]).

bernoulli(0) -> 1.0;
bernoulli(N) -> - sigma(0, N, 0) / (N + 1).

sigma(K, M, S) when K == M -> S;
sigma(K, M, S) when K < M -> sigma(K+1, M, S + binom(M+1,K)*bernoulli(K)).
    
binom(N,K) ->
    if (K < 0) or (K > N) -> 0;
       (N == K) or (K == 0) -> 1;
       true -> Mult = fun (A, B) -> A * B end,
               lists:foldl(Mult,1,lists:seq((N-K+1),N))
                   /
                   lists:foldl(Mult,1,lists:seq(1,K))
    end.
