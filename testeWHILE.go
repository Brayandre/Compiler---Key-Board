package main

import "fmt"

func main() {
	var x int = 0
	var limite int = 5
	for x < limite {
		fmt.Println("valor de x", x)
		x = x + 1
	}
}
