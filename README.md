
[![CI](https://github.com/andresserranodev/POC-DigitClassifier/actions/workflows/CI.yml/badge.svg)](https://github.com/andresserranodev/POC-DigitClassifier/actions/workflows/CI.yml)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io)

# Digit Classifier Android App

This repository contains an Android application that demonstrates the implementation of a Convolutional Neural Network (CNN) for handwritten digit classification. The project serves educational purposes, showcasing how to train a neural network and integrate it into an Android application.
[PPP](https://docs.google.com/presentation/d/1s2gGfQZfQN1WPaiPBY6wjbwxg20DGCKVFaIyTeB805A/edit?usp=sharing)

## Overview

The application is built using Jetpack Compose and implements a CNN model trained on the MNIST dataset. The model was trained using Google Colab, and the resulting TensorFlow Lite model is integrated into the Android application.

## Preview
![Preview](https://github.com/andresserranodev/POC-DigitClassifier/blob/main/previews/preview.gif)

## Features

- Modern UI built with Jetpack Compose
- Real-time digit classification
- Integration of TensorFlow Lite model
- Clean architecture implementation

## Technical Details

### Model Training
The CNN model was trained using the MNIST dataset in Google Colab. You can find the training notebook here:
[Training Notebook](https://colab.research.google.com/drive/1UJ-jONtH3Kb-CWAHNGCfUMI08sZBOs3_#scrollTo=9e9huQCrx7wV)

### Android Implementation
- Built using Jetpack Compose for modern UI development
- Uses TensorFlow Lite for model inference

## Project Structure

The project follows a clean architecture approach with the following main components:
- UI Layer (Compose)
- Model Integration

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Build and run the application

## Dependencies

The project uses the following main dependencies:
- Jetpack Compose
- TensorFlow Lite
- AndroidX Core
- Material Design Components

## Learning Resources

This project was inspired by various sources and learning materials:

1. [A Proposal for the Dartmouth Summer Research Project on Artificial Intelligence](http://jmc.stanford.edu/articles/dartmouth.html)
2. [Levels of AGI for Operationalizing Progress on the Path to AGI](https://arxiv.org/abs/2311.02462)
3. [Build a handwritten digit classifier app with TensorFlow Lite](https://developer.android.com/codelabs/digit-classifier-tflite#4)
4. [Google Machine Learning Crash Course](https://developers.google.com/machine-learning/crash-course)
5. [APRENDE ¿Qué son las Redes Neuronales? de Dot CSV](https://www.youtube.com/playlist?list=PL-Ogd76BhmcB9OjPucsnc2-piEE96jJDQ)
6. [Introduction to Generative AI](https://www.cloudskillsboost.google/paths/118/course_templates/536)
7. [The Impact of Generative AI on Critical Thinking](https://www.microsoft.com/en-us/research/publication/the-impact-of-generative-ai-on-critical-thinking-self-reported-reductions-in-cognitive-effort-and-confidence-effects-from-a-survey-of-knowledge-workers/)
8. [Ask a Techspert: What is generative AI?](https://blog.google/inside-google/googlers/ask-a-techspert/what-is-generative-ai/)
9. [Con Jetpack Compose , lleva el desarrollo de interfaces al siguiente nivel](https://devexpert.io/compose-expert/)

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. 
