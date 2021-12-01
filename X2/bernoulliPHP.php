<?php
function main()
{
    $n = (int) fgets(STDIN);
    if ($n == 0) {echo "1\n"; return;}
    $n++;
    $b[] = array();
    $b[] = 1;
    foreach (range (2, $n) as $m) {
        $b[] = 0;
        foreach (range (1, ($m - 1)) as $k) {
            $b[$m] -= binom($m, $k-1)*$b[$k];
        }
        $b[$m] /= $m;
    }
    echo $b[$n];
    echo "\n";
}

function binom($n, $k)
{
    $r = 1;
    if ($n == $k || $k == 0) return 1;
    foreach (range (1, $k) as $i) {
        $r *= ($n - $i + 1) / $i;
    }
    return $r;
}

main();
?>
