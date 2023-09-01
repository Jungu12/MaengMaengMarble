from PyPDF2 import PdfFileReader

pdf_file = open('230814_프로젝트 산출물 안내.pdf', 'rb')

pdf_reader = PdfFileReader(pdf_file)

page = pdf_reader.getPage(0)
text = page.extract_text()
print(text)

pdf_file.close()