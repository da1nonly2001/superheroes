<!DOCTYPE html>
<html>
	<head>		
		<link rel="stylesheet" href="web/resources/styles/hvh.css">
	</head>
	<body>
		<div class="pageContainer">
			<div class="pageHeader"></div>
			<div id="menu">
				<div id="home" class="menuItem"></div>
				<div id="categories" class="menuItem"></div>
			</div>
			<div id="content">
				
			</div>
			<div class="pageFooter">Powered by Electricity &copy;</div>
		</div>
	</body>
        <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
        <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
        <script src="web/resources/scripts/hvh.js"></script>
	<script>
            $(document).ready(function(){
                
                loadContent("home");

                $(".pageHeader").click(function(){
                    loadContent("home");
                });
                
                $("#home").click(function(){
                    loadContent("home");
                });

                $("#categories").click(function(){
                    loadContent("categories");
                });
            });
	</script>
</html>