grammar Exp;

file returns [ru.hse.spb.Block value]
    :   b=block EOF {$value = $b.value;}
    ;

block returns [ru.hse.spb.Block value]
    :   {$value = new ru.hse.spb.Block();}
        (s=statement {$value.add($s.value);})*
    ;

blockWithBraces returns [ru.hse.spb.Block value]
    :   '{' b=block '}' {$value = $b.value;}
    ;

statement returns [ru.hse.spb.Statement value]
    :   (s=functionDefinition {$value = $s.value;}
        | v=variable {$value = $v.value;}
        | w=whileLoop {$value = $w.value;}
        | e=expression {$value = $e.value;}
        | c=conditional {$value = $c.value;}
        | a=assignment {$value = $a.value;}
        | r=returnStatement {$value = $r.value;}
        | p=printStatement {$value = $p.value;})
    ;

functionDefinition returns [ru.hse.spb.FunctionDefinition value]
    :    'fun' name=IDENTIFIER '(' pars=parameterNames ')' b=blockWithBraces {$value = new ru.hse.spb.FunctionDefinition($name.text, $pars.list, $b.value);}
    ;

variable returns [ru.hse.spb.Variable value]
    :    'var' name=IDENTIFIER ('=' exp=expression)? {$value = new ru.hse.spb.Variable($name.text, $exp.value);}
    ;

parameterNames returns [java.util.List<String> list]
    :   {$list = new java.util.ArrayList<>();}
        (name=IDENTIFIER {$list.add($name.text);}
            (',' name=IDENTIFIER {$list.add($name.text);}
            )*
        )?
    ;

whileLoop returns [ru.hse.spb.WhileLoop value]
    :   'while' '(' exp=expression ')' b=blockWithBraces {$value = new ru.hse.spb.WhileLoop($exp.value, $b.value);}
    ;

conditional returns [ru.hse.spb.Conditional value]
    :   'if' '(' exp=expression ')' b1=blockWithBraces {$value = new ru.hse.spb.Conditional($exp.value, $b1.value);}
        ('else' b2=blockWithBraces {$value = new ru.hse.spb.Conditional($exp.value, $b1.value, $b2.value);})?
    ;

assignment returns [ru.hse.spb.Assignment value]
    :   name=IDENTIFIER '=' exp=expression {$value = new ru.hse.spb.Assignment($name.text, $exp.value);}
    ;

returnStatement returns [ru.hse.spb.ReturnStatement value]
    :   'return' exp=expression {$value = new ru.hse.spb.ReturnStatement($exp.value);}
    ;

expression returns [ru.hse.spb.Expression value]
    :   f=functionCall  {$value = $f.value;}
        | <assoc=left> left=expression (op='*' | op='/' | op='%') right=expression {$value = new ru.hse.spb.BinaryExpression($left.value, $right.value, $op.text);}
        | <assoc=left> left=expression (op='+' | op='-') right=expression {$value = new ru.hse.spb.BinaryExpression($left.value, $right.value, $op.text);}
        | left=expression (op='>=' | op='>' | op='<=' | op='<') right=expression {$value = new ru.hse.spb.BinaryExpression($left.value, $right.value, $op.text);}
        | left=expression (op='&&' | op='||') right=expression {$value = new ru.hse.spb.BinaryExpression($left.value, $right.value, $op.text);}
        | name=IDENTIFIER {$value = new ru.hse.spb.IdentifierExpression($name.text);}
        | number=LITERAL {$value = new ru.hse.spb.LiteralExpression(Integer.parseInt($number.text));}
        | '(' e=expression ')' {$value = $e.value;}
    ;

functionCall returns [ru.hse.spb.FunctionCall value]
    :   name=IDENTIFIER '(' args=arguments ')' {$value = new ru.hse.spb.FunctionCall($name.text, $args.list);}
    ;

arguments returns [java.util.List<ru.hse.spb.Expression> list]
    :   {$list = new java.util.ArrayList<>();}
        (e=expression {$list.add($e.value);}
            (',' e=expression {$list.add($e.value);}
            )*
        )?
    ;

printStatement returns [ru.hse.spb.PrintStatement value]
    :   'println' '(' args=arguments ')' {$value = new ru.hse.spb.PrintStatement($args.list);}
    ;

IDENTIFIER
    :   (('a'..'z')|('A'..'Z')|'_')
        (('a'..'z')|('A'..'Z')|('0'..'9')|'_')*
    ;

LITERAL
    :   '0'|(('1'..'9')('0'..'9')*)
    ;

WS : (' ' | '\t' | '\r'| '\n') -> skip;
COMMENT : '//'(.)*? ('\n'|EOF) -> skip;