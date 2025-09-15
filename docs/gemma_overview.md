# Gemma-3n Overview

- **Model sizes**: Gemma-3n comes in “E2B” (effective 2B parameters) and “E4B” (effective 4B). The “effective” tag means they get similar quality to a bigger dense model by only activating parts of the network.

- **Memory with quantization**:  
  • 4B full precision ≈ 6.4 GB  
  • 4B 8-bit ≈ 4.4 GB  
  • 4B 4-bit ≈ 3.4 GB  
  • E2B 4-bit ≈ 2 GB  
  • E4B 4-bit ≈ 3 GB

- **Context length**: Supports long windows (up to ~32K tokens) which is plenty for chat or docs. Longer windows increase temporary memory needs.

- **Quantization-aware training**: Many Gemma-3 / Gemma-3n releases are trained to behave better when quantized, so the quality drop from 16-bit → 4-bit is smaller than you’d expect.

- **Practical use**:  
  • E2B 4-bit can run on ~2 GB RAM, so it fits into mid-range phones for text-only.  
  • Larger or multimodal variants need more RAM and cooling.  
  • Good candidates for on-device assistants and lightweight LLM demos.
