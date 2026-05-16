package main

import "fmt"

func main() {
	var x int = 0
	var y int = 0
	var limite int = 5
	for x < limite {
		fmt.Println("valor de x", x)
		for y < limite {
			fmt.Println("valor de y", y)
			y = y + 1
		}
		x = x + 1
	}
}
