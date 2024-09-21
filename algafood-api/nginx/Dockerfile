FROM nginx:1.27.1-alpine

# Remoção/substituição do arquivo de configuração padrão
RUN rm /etc/nginx/conf.d/default.conf

COPY nginx.conf /etc/nginx/conf.d/default.conf