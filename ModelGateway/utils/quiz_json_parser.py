import json
import re
from typing import Any

from langchain.output_parsers import OutputFixingParser
from pydantic import Field


class QuizJSONParser(OutputFixingParser):
    parser: Any = Field(default=None)
    retry_chain: Any = Field(default=None)

    def parse(self, text: str) -> dict:
        match = re.search(r"```json\s*(.*?)\s*```", text, re.DOTALL)
        if not match:
            raise ValueError("JSON not found in response")

        json_str = match.group(1).strip()
        try:
            return json.loads(json_str)
        except json.JSONDecodeError as e:
            raise ValueError(f"Invalid JSON: {e}")
