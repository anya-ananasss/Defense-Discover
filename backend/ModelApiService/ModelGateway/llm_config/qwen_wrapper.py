from qwen_api.client import Qwen
from qwen_api.core.types.chat import ChatMessage, MessageRole
from langchain_core.runnables import Runnable
from typing import Any, Dict, Optional, Union
from langchain_core.prompt_values import StringPromptValue


class QwenWrapper(Runnable):
    def __init__(self):
        self.client = Qwen()

    def generate(self, prompt: str):
        messages = [
            ChatMessage(
                role=MessageRole.USER,
                content=prompt,
                web_search=True,
                thinking=False,
            )
        ]

        response = self.client.chat.create(
            messages=messages,
            model="qwen-max-latest",
            stream=True,
        )

        if not hasattr(response, '__iter__'):
            raise ValueError("Response is not iterable.")

        assistant_reply = ""

        for chunk in response:
            if chunk.choices:
                for choice in chunk.choices:
                    if choice.delta and choice.delta.content:
                        assistant_reply += choice.delta.content

        return assistant_reply

    def invoke(self, input_data: Union[Dict[str, Any], StringPromptValue], config: Optional[Dict] = None) -> str:
        if isinstance(input_data, StringPromptValue):
            prompt = input_data.to_string()
        else:
            raise ValueError(f"Unsupported input type: {type(input_data)}")

        return self.generate(prompt)

    @property
    def InputType(self):
        from langchain_core.pydantic_v1 import BaseModel as LangChainBaseModel

        class Input(LangChainBaseModel):
            topic: str
            num_questions: int
            difficulty: str
            key_words: str

        return Input

    @property
    def OutputType(self):
        return Dict[str, Any]
