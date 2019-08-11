package me.chenqiang.pdf.composer.functional;

import me.chenqiang.pdf.DocumentContext;
import me.chenqiang.pdf.component.PdfElementComposer;

@FunctionalInterface interface ConditionalProducer {
	public void accept(@SuppressWarnings("rawtypes") PdfElementComposer composer, DocumentContext context);
}