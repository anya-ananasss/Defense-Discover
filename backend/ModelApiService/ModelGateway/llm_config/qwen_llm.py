from langchain.llms.base import LLM
from pydantic import Field
from typing import Optional, List, Mapping, Any
import requests


class QwenLLM(LLM):
    api_token: Optional[str] = Field(None, description="Bearer token for QWEN API (if required)")
    api_url: str = Field(
        "http://qwen:8000/v1/chat/completions",
        description="Full URL for the QWEN OpenAI-compatible chat completions endpoint inside Docker network"
    )
    model_name: str = Field(
        "qwen2.5-chat",
        description="Default model name to use for chat (adjust as needed)"
    )

    def _call(
        self,
        prompt: str,
        stop: Optional[List[str]] = None,
        run_manager: Optional[Any] = None,
        **kwargs
    ) -> str:
        # Construct the payload according to OpenAI Chat API spec
        payload = {
            "model": self.model_name,
            "messages": [
                {"role": "user", "content": prompt}
            ],
            "stream": False,
        }
        headers = {
            "Content-Type": "application/json",
        }
        if self.api_token:
            headers["Authorization"] = f"Bearer {self.api_token}"

        response = requests.post(
            self.api_url,
            headers=headers,
            json=payload,
            timeout=30,
        )

        if response.status_code != 200:
            raise ValueError(f"QWEN API Error [{response.status_code}]: {response.text}")

        data = response.json()
        try:
            # Extract assistant content from response
            return data["choices"][0]["message"]["content"]
        except (KeyError, IndexError) as e:
            raise ValueError(f"Unexpected QWEN response format: {data}")

    @property
    def _llm_type(self) -> str:
        return "qwen"

    @property
    def _identifying_params(self) -> Mapping[str, Any]:
        token_repr = self.api_token[:5] + "..." if self.api_token else None
        return {
            "model_name": self.model_name,
            "api_url": self.api_url,
            "api_token": token_repr,
        }
