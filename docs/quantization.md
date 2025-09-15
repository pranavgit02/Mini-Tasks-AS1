# Quantization Basics 

Quantization reduces the precision of model weights, lowering memory and compute needs.

| Model Size | Precision | RAM Needed | Notes |
|------------|-----------|------------|-------|
| 2B params  | 8-bit     | ~2 GB      | Baseline FP8 |
| 2B params  | 4-bit     | ~1 GB      | 50% smaller |
| 7B params  | 8-bit     | ~7 GB      | Needs high-end GPU |
| 7B params  | 4-bit     | ~3.5 GB    | Fits on laptop GPU |
| 13B params | 8-bit     | ~13 GB     | Not practical on mobile |
| 13B params | 4-bit     | ~6.5 GB    | Possible on desktop w/ 8 GB GPU |
