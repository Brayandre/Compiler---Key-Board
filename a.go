package main

import "fmt"

func main() {
	var a int
	var b int
	var c int
	fmt.Println("Digite o numero a")
	fmt.Scan(&a)
	fmt.Println("Digite o numero b")
	fmt.Scan(&b)
	fmt.Println("Digite o numero c")
	fmt.Scan(&c)
	var abc int
	abc = (a + b) * c
	fmt.Println(abc)
}
