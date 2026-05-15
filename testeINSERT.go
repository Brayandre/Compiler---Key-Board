package main

import "fmt"

func main() {
	var x int
	var y int
	fmt.Println("Digite o primeiro numero")
	fmt.Scan(&x)
	fmt.Println("Digite o segundo numero")
	fmt.Scan(&y)
	if x < y {
		fmt.Println("x e menor que y", x)
	} else if x == y {
		fmt.Println("x e igual a y", x)
	} else {
		fmt.Println("x e maior que y", x)
	}
}
