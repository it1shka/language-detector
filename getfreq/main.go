package main

import (
	"log"
	"os"
	"path/filepath"
	"strings"
	"sync"
)

func DirectoryIntoFrequencies(directory, outname string) {
	files, err := os.ReadDir(directory)
	if err != nil {
		log.Fatal(err)
	}
	if err := os.MkdirAll(outname, os.ModePerm); err != nil {
		log.Fatal(err)
	}
	var wg sync.WaitGroup
	wg.Add(len(files))
	for _, file := range files {
		filename := file.Name()
		nameWithoutExt := strings.TrimSuffix(filename, filepath.Ext(filename))
		sourcepath := filepath.Join(directory, filename)
		targetpath := filepath.Join(outname, nameWithoutExt+".freqs")
		go func() {
			defer wg.Done()
			DocIntoFrequencies(sourcepath, targetpath)
		}()
	}
	wg.Wait()
}

func main() {
	dir := "./sources"
	if len(os.Args) > 1 {
		dir = os.Args[1]
	}
	DirectoryIntoFrequencies(dir, "./.language_data")
}
