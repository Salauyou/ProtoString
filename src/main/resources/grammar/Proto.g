grammar Proto;

obj
  : '{' keyValue* '}'
  ;

keyValue
  : ID ':' scalar
  | ID obj
  ;

scalar
  : STRING     #string
  | NUMBER     #number
  | ID         #enum
  | 'true'     #true
  | 'false'    #false
  ;

ID
  : [a-zA-Z][a-zA-Z0-9_]*
  ;

STRING
  : '"' (ESC | CHAR)* '"'
  ;

fragment ESC
  : '\\' ["\\/bnrt]
  ;

fragment CHAR
  : ~["\\]
  ;

NUMBER
  : '-'? INT ('.' [0-9] +)?
  ;

fragment INT
  : '0' | [1-9] [0-9]*
  ;

WS
  : [ \t\n\r] + -> skip
  ;