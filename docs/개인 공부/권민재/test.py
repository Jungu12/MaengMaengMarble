import os
import re
import requests
from bs4 import BeautifulSoup
from my_PyPDF2 import PdfReader
from pytube import Playlist
from youtube_transcript_api import YouTubeTranscriptApi
import streamlit as st
from st_chat_message import message
from langchain.chat_models import ChatOpenAI
from langchain.chains import LLMChain
from langchain.document_loaders import TextLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.vectorstores.faiss import FAISS
from langchain.prompts.chat import (
    ChatPromptTemplate,
    SystemMessagePromptTemplate,
    HumanMessagePromptTemplate,
)
