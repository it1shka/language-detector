package main

import (
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"strings"

	"code.sajari.com/docconv"
)

func DocIntoFrequencies(source, target string) {
	freqmap := GetFrequencyMap(source)
	if err := SaveInPairsFormat(freqmap, target); err != nil {
		fmt.Printf("Failed to save freqmap for %s: %s\n", source, err)
	}
}

func ReadDoc(docpath string) (string, error) {
	fmt.Printf("Reading %s\n", docpath)
	res, err := docconv.ConvertPath(docpath)
	if err != nil {
		return "", err
	}
	return res.Body, nil
}

func SaveInPairsFormat(freqmap map[rune]float64, filename string) error {
	file, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer file.Close()
	pairs := make([]string, len(freqmap))
	index := 0
	for letter, freq := range freqmap {
		pair := fmt.Sprintf("%c %f", letter, freq)
		pairs[index] = pair
		index++
	}
	str := strings.Join(pairs, "\n")
	_, err = file.WriteString(str)
	return err
}

var DocumentExts = []string{
	".pdf", ".doc", ".docx",
	".xml", ".xml", ".html",
	".rtf", ".odt", ".txt",
}

func IsDocument(extension string) bool {
	for _, docext := range DocumentExts {
		if extension == docext {
			return true
		}
	}
	return false
}

func FindAllDocuments(root string) []string {
	documents := []string{}
	filepath.Walk(root, func(path string, info fs.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() {
			return nil
		}
		ext := filepath.Ext(info.Name())
		if IsDocument(ext) {
			documents = append(documents, path)
		}
		return nil
	})
	return documents
}
