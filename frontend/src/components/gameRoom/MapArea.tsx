import { useCallback } from 'react';

type Props = {
  height?: number;
  width?: number;
  src: string;
  alt: string;
  value: number;
};

const MapArea = ({ height, width, src, alt, value }: Props) => {
  const onClickArea = useCallback(() => {
    console.log(value);
  }, [value]);

  return (
    <button onClick={onClickArea}>
      <img
        src={src}
        alt={alt}
        style={{
          width: `${width}px`,
          height: `${height}px`,
        }}
      />
    </button>
  );
};

export default MapArea;
