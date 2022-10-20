package main

import (
	"fmt"
	"strings"
	"sync"
	"unicode"
)

func GetFrequencyMap(directiory string) map[rune]float64 {
	documents := FindAllDocuments(directiory)
	countmaps := make(chan map[rune]int, 10)
	var wg sync.WaitGroup
	wg.Add(len(documents))
	for _, doc := range documents {
		go CountLettersInPDF(doc, countmaps, &wg)
	}
	go func() {
		defer close(countmaps)
		wg.Wait()
	}()
	totlaAllLetters := 0
	totalCounter := make(map[rune]int)
	for chunk := range countmaps {
		for letter, count := range chunk {
			totlaAllLetters += count
			totalCounter[letter] += count
		}
	}
	freqmap := make(map[rune]float64)
	for letter, count := range totalCounter {
		freqmap[letter] = 100.0 * float64(count) / float64(totlaAllLetters)
	}
	return freqmap
}

func CountLettersInPDF(document string, out chan map[rune]int, wg *sync.WaitGroup) {
	defer wg.Done()
	text, err := ReadDoc(document)
	if err != nil {
		fmt.Printf("Error occured:\n%s\n", err)
		return
	}
	counter := make(map[rune]int)
	for _, char := range strings.ToLower(text) {
		if !unicode.IsLetter(char) {
			continue
		}
		counter[char]++
	}
	out <- counter
}
