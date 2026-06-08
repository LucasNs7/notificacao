package com.lucas.notificacoes.business.service;

import com.lucas.notificacoes.business.dto.TarefaDTO;
import com.lucas.notificacoes.infra.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${envio.email.from}")
    public String from;

    @Value("${envio.email.name}")
    public String name;

    public void enviaEmail(TarefaDTO tarefaDTO) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(
                    mensagem, true,
                    StandardCharsets.UTF_8.name()
            );

            mimeHelper.setFrom(new InternetAddress(from, name));
            mimeHelper.setTo(InternetAddress.parse(tarefaDTO.getUsuarioEmail()));
            mimeHelper.setSubject("Notificação de Tarefa");

            Context context = new Context();
            context.setVariable("nomeTarefa", tarefaDTO.getNomeTarefa());
            context.setVariable("dataEvento", tarefaDTO.getDataEvento());
            context.setVariable("descricao", tarefaDTO.getDescricao());
            String template = templateEngine.process("notificacao", context);
            mimeHelper.setText(template, true);
            javaMailSender.send(mensagem);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar email ", e.getCause());
        }
    }
}
