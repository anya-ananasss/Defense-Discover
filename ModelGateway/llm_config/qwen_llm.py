from langchain.llms.base import LLM
from pydantic import Field
from typing import Optional, List, Mapping, Any
import requests


class QwenLLM(LLM):
    api_token: str = Field(...)
    api_url: str = Field(...)
    model_name: str = Field(...)

    def _call(self, prompt: str, stop: Optional[List[str]] = None, run_manager: Optional[Any] = None, **kwargs) -> str:
        payload = {
            "chat_type": "t2t",
            "messages": [{"role": "user", "content": prompt}],
            "model": self.model_name,
            "stream": False,
        }

        response = requests.post(
            self.api_url,
            headers={
                "Authorization": f"Bearer {self.api_token}",
                "Content-Type": "application/json",
            },
            json=payload,
            timeout=30,
        )

        if response.status_code != 200:
            raise ValueError(f"API Error: {response.text}")

        try:
            content = response.json()["choices"][0]["message"]["content"]
            return content
        except KeyError:
            raise ValueError("Invalid response format")

    @property
    def _llm_type(self) -> str:
        return "qwen"

    @property
    def _identifying_params(self) -> Mapping[str, Any]:
        return {
            "model_name": self.model_name,
            "api_token": self.api_token[:5] + "...",
        }
