package main

import "fmt"

func main() {
	var contador int = 0
	var limite int
	limite = 10
	for contador < limite {
		fmt.Println("valor: ", contador)
		contador = contador + 1
	}
	if contador == limite {
		fmt.Println("terminou")
	} else {
		fmt.Println("nao chegou")
	}
	for i := 0; i < 5; i++ {
		fmt.Println("i = ", i)
	}
}
