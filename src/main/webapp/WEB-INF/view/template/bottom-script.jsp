<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="static/js/jquery.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/jquery.scrollUp.min.js"></script>
<script src="static/js/price-range.js"></script>
<script src="static/js/jquery.prettyPhoto.js"></script>
<script src="static/js/main.js"></script>
<script>

    refreshMenuCart();

    function refreshMenuCart() {
        $.ajax({
            url: 'cart-product-count',
            type: 'POST',
        }).done(function(value){
            if(0 != value) {
                $('#cart_ico a span').html('');
                $('#cart_ico a').append('<span class="badge" style="margin-left: 5px; background-color: #ff4040;">'+ value +'</span>');
            } else {
                $('#cart_ico a span').html('');
            }
        });
    };

    // language selection

    $(document).ready(function(){

        $("#lang-select").on("change", (function() {
            $(this).children(":selected").trigger("click");
        }));

        $('#lang-select option').click(function(){
            let lang = $(this).attr("value");
            let pathName = $(location).attr('href')

            if(pathName.includes('lang=')) {
                pathName = pathName.replace(/lang=../, 'lang=' + lang);
            } else {
                pathName = pathName.indexOf('?') == -1 ? pathName + '?lang=' + lang : pathName + '&lang=' + lang;
            }

            window.location.href = pathName;
        });
    });
</script>