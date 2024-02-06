<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Listado de Artículos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <h2>Listado de Artículos - ${usuario.full_name}</h2>

    <#if articulos?size != 0>
        <table class="table table-bordered">
            <thead>
            <tr style="background-color: #18406a; color: #fff;">
                <th>ID</th>
                <th>Nombre</th>
                <th>Valor</th>
                <#if usuario.id != user_logged>
                    <th>Comprar</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#list articulos as articulo>
                <tr style="background-color: #f9f9f9">
                    <td>${articulo.id}</td>
                    <td>${articulo.name_}</td>
                    <td>${articulo.value_}€</td>
                    <#if usuario.id != user_logged>
                        <td><a href="/comprar/${usuario.id}/${articulo.id}">COMPRAR ARTÍCULO</a></td>
                    </#if>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>Oh... parece que no dispone de articulos</p>
    </#if>
    <a href="/home" class="btn btn-outline-info btn-lg">Home</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>