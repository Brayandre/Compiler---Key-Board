package main

import "fmt"

func main() {
	var n1 int
	var n2 int = 13
	n1 = 32
	if n1 < n2 {
		n1 = 62
	}
	fmt.Println("resultado", n1)
}
