<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Listado de Registros</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <h2>Listado de Registros - ${usuario.full_name}</h2>
    </br>
    </br>
    <#if compras?size != 0 || ventas?size != 0>
        <#if compras?size != 0>
            <h3>Compras realizadas - ${usuario.full_name}</h3>
            <table class="table table-bordered">
                <thead>
                <tr style="background-color: #18406a; color: #fff;">
                    <th>ID</th>
                    <th>Objeto (ID)</th>
                    <th>Comprador (ID)</th>
                    <th>Vendedor (ID)</th>
                    <th>Día de la venta</th>
                </tr>
                </thead>
                <tbody>
                <#list compras as order>
                    <tr style="background-color: #f9f9f9">
                        <td>${order.id}</td>
                        <td>${order.objectID}</td>
                        <td><a href="/usuario/${order.userC}">${order.userC}</a></td>
                        <td><a href="/usuario/${order.userV}">${order.userV}</a></td>
                        <td>${order.date_order}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </#if>
        </br>
        <#if ventas?size != 0>
            <h3>Ventas realizadas - ${usuario.full_name}</h3>
            <table class="table table-bordered">
                <thead>
                <tr style="background-color: #18406a; color: #fff;">
                    <th>ID</th>
                    <th>Objeto (ID)</th>
                    <th>Comprador (ID)</th>
                    <th>Vendedor (ID)</th>
                    <th>Día de la venta</th>
                </tr>
                </thead>
                <tbody>
                <#list ventas as order>
                    <tr style="background-color: #f9f9f9">
                        <td>${order.id}</td>
                        <td>${order.objectID}</td>
                        <td><a href="/usuario/${order.userC}">${order.userC}</a></td>
                        <td><a href="/usuario/${order.userV}">${order.userV}</a></td>
                        <td>${order.date_order}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </#if>
    <#else>
        <p>Oh... parece que no ha realizado ningún movimiento</p>
    </#if>
    <a href="/home" class="btn btn-outline-info btn-lg">Home</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>